package loading.mixins;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.pig.pack.api.Globals;
import loading.events.PacketEvent;

@Mixin( NetworkManager.class )
public class MixinNetworkManager implements Globals {

    @Inject( method = "channelRead0", at = @At( "HEAD" ), cancellable = true )
    public void onPacketReceive( final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo ci ) {
        if ( mc.player == null && mc.world == null ) return;
        final PacketEvent.Receive event = new PacketEvent.Receive( packet );
        MinecraftForge.EVENT_BUS.post( event );
        if ( event.isCanceled( ) ) {
            ci.cancel( );
        }
    }

    @Inject( method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At( "HEAD" ), cancellable = true )
    public void onPacketSend( final Packet<?> packet, final CallbackInfo ci ) {
        if ( mc.player == null && mc.world == null ) return;
        final PacketEvent.Send event = new PacketEvent.Send( packet );
        MinecraftForge.EVENT_BUS.post( event );
        if ( event.isCanceled( ) ) {
            ci.cancel( );
        }
    }
}